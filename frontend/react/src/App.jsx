import { Spinner, Text, Wrap, WrapItem } from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import { getCustomers} from "./services/client.js";
import CardWithImage from "./components/CustomerCard.jsx";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/Notification.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("")
    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then( res => {
            setCustomers(res.data);
        }).catch(err => {
            setError(err.response.data.message);
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(()=>
            setLoading(false)
        )
    }

    useEffect(() => {
        fetchCustomers()
    }, [])

    if(loading){
        return(
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if(err){
        return(
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>Ooops there was an  error</Text>
            </SidebarWithHeader>
        )
    }

    if(customers.length <= 0){
        return(
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>No customers available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Wrap justify={"centre"} spacing={"5"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            customer={customer}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export  default App;